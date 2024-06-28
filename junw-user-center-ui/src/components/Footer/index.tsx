import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = 'Junw User Center';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'uxin online',
          title: '优X小宇宙',
          href: 'http://uxin.online/',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <><GithubOutlined /> Junw GitHub</>,
          href: 'https://github.com/CallMeLD/Junw-UserCenter',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
